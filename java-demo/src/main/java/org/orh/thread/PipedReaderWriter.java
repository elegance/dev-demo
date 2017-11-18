package org.orh.thread;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class PipedReaderWriter {

    public static void main(String[] args) throws IOException, InterruptedException {
        WriteData writeData = new WriteData();
        ReadData readData = new ReadData();

        PipedReader inputStream = new PipedReader();
        PipedWriter outputStream = new PipedWriter();
        outputStream.connect(inputStream);

        Thread readThread = new Thread(() -> {
            readData.readMethod(inputStream);
        });
        readThread.start();
        Thread.sleep(2000);

        Thread writeThread = new Thread(() -> {
            writeData.writeMethod(outputStream);
        });
        writeThread.start();
    }

    static class WriteData {
        public void writeMethod(PipedWriter out) {
            try {
                System.out.println("write:");
                for (int i = 0; i < 300; i++) {
                    String outData = "" + (i + 1);
                    out.write(outData);
                    System.out.print(outData);
                }
                System.out.println();
                out.close();
            } catch (Exception e) {
            }
        }
    }

    static class ReadData {
        public void readMethod(PipedReader input) {
            try {
                System.out.println("read :");
                char[] charArr = new char[20];
                int readLen = input.read(charArr);
                while (readLen != -1) {
                    String newData = new String(charArr, 0, readLen);
                    System.out.print(newData);
                    readLen = input.read(charArr);
                }
                input.close();
            } catch (Exception e) {
            }
        }
    }
}
