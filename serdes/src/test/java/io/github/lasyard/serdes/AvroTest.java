package io.github.lasyard.serdes;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.util.Utf8;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

public class AvroTest {
    @Test
    public void test() throws IOException {
        User user = new User();
        user.setName("Alice");
        user.setScore(100);
        ByteBuffer buffer = user.toByteBuffer();
        byte[] byteArray = buffer.array();
        User user1 = User.fromByteBuffer(ByteBuffer.wrap(byteArray));
        assertThat(user1.getName().toString()).isEqualTo("Alice");
        assertThat(user1.getScore()).isEqualTo(100);
    }

    @Test
    public void test1() throws IOException {
        User user = new User("Alice", 100);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        user.writeExternal(oos);
        oos.close();
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        User user1 = new User();
        user1.readExternal(ois);
        assertThat(user1).isEqualTo(user);
    }

    @Test
    public void test2() throws IOException {
        Schema schema = new Schema.Parser().parse(new File("src/main/avro/user.avsc"));
        GenericRecord user = new GenericData.Record(schema);
        user.put("name", "Alice");
        user.put("score", 100);
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        datumWriter.write(user, EncoderFactory.get().directBinaryEncoder(out, null));
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        byte[] result = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(result);
        GenericRecord user1 = datumReader.read(null, DecoderFactory.get().directBinaryDecoder(in, null));
        Object name = user1.get("name");
        assertThat(name).isInstanceOf(Utf8.class);
        assertThat(name.toString()).isEqualTo("Alice");
        Object score = user1.get("score");
        assertThat(score).isInstanceOf(Integer.class);
        assertThat(score).isEqualTo(100);
    }
}
