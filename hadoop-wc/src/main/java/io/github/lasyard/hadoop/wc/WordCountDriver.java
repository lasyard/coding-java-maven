package io.github.lasyard.hadoop.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivilegedExceptionAction;
import javax.annotation.Nonnull;

/**
 * This is word count using map-reduce on yarn.
 *
 * <p>Run with:
 * {@code yarn jar hadoop-wc-1.0.0-SNAPSHOT.jar}
 */
public class WordCountDriver extends Configured implements Tool {
    public static final int BUFF_SIZE = 256;
    private static final String INPUT_FILE = "test.txt";
    private static final String OUTPUT_DIR = "test-output";

    public static void main(String[] args) throws Exception {
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
        System.exit(ugi.doAs((PrivilegedExceptionAction<Integer>) () -> {
            System.setProperty("hadoop.home.dir", "/");
            Configuration configuration = new Configuration();
            configuration.addResource("server.xml");
            FileSystem fs = FileSystem.get(configuration);
            fs.delete(new Path(OUTPUT_DIR), true);
            // Copy input file
            FSDataOutputStream out = fs.create(new Path(INPUT_FILE), true);
            InputStream in = WordCountDriver.class.getResourceAsStream("/" + INPUT_FILE);
            assert in != null;
            IOUtils.copyBytes(in, out.getWrappedStream(), BUFF_SIZE);
            in.close();
            out.close();
            final int exitCode = ToolRunner.run(
                configuration,
                new WordCountDriver(),
                new String[]{INPUT_FILE, OUTPUT_DIR}
            );
            fs.delete(new Path(INPUT_FILE), true);
            FSDataInputStream res = fs.open(new Path(OUTPUT_DIR + "/part-r-00000"));
            IOUtils.copyBytes(res, System.out, BUFF_SIZE);
            fs.delete(new Path(OUTPUT_DIR), true);
            return exitCode;
        }));
    }

    @Override
    public int run(@Nonnull String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length < 2) {
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }
        Job job = Job.getInstance(getConf());
        job.setJobName("WordCount");
        job.setJarByClass(WordCountDriver.class);
        job.setMapperClass(WordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
