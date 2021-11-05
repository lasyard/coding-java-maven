package io.github.lasyard.serdes;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class KryoTest {
    private static final Kryo kryo = new Kryo();

    @BeforeAll
    public static void setup() {
        kryo.register(Model.class);
    }

    @Test
    public void test() {
        Model model = new Model();
        model.setName("Alice");
        model.setScore(100);
        Output output = new Output(1024);
        kryo.writeClassAndObject(output, model);
        log.info("The size of output is {}.", output.total());
        Input input = new Input(output.getBuffer());
        Model model1 = (Model) kryo.readClassAndObject(input);
        assertThat(model1).isEqualTo(model);
    }

    @Test
    public void test1() {
        Model model = new Model();
        model.setName("Alice");
        model.setScore(100);
        Output output = new Output(1024);
        kryo.writeObject(output, model);
        log.info("The size of output is {}.", output.total());
        Input input = new Input(output.getBuffer());
        Model model1 = kryo.readObject(input, Model.class);
        assertThat(model1).isEqualTo(model);
    }
}
