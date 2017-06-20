package org.orh.thread;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedInputOutput {

    public static void main(String[] args) throws IOException, InterruptedException {
        WriteData writeData = new WriteData();
        ReadData readData = new ReadData();

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream();

        outputStream.connect(inputStream);

        Thread readThread = new Thread(() -> {
            readData.readMethod(inputStream);
        }, "read-thread");
        readThread.start();

        Thread.sleep(2000);
        Thread writeThread = new Thread(() -> {
            writeData.writeMethod(outputStream);
        }, "write-thread");
        writeThread.start();
    }

    static class WriteData {
        public void writeMethod(PipedOutputStream out) {
            try {
                System.out.println("write :");
                for (int i = 0; i < 300; i++) {
                    String outData = "" + (i + 1);
                    out.write(outData.getBytes());
                    System.out.print(outData);
                }
                System.out.println();
                out.close();
            } catch (Exception e) {
            }
        }
    }

    static class ReadData {
        public void readMethod(PipedInputStream input) {
            try {
                System.out.println("read  :");
                byte[] byteArray = new byte[20];
                int readLength = input.read(byteArray);
                while (readLength != -1) {
                    String newData = new String(byteArray, 0, readLength);
                    System.out.print(newData);
                    readLength = input.read(byteArray);
                }
                System.out.println();
                input.close();
            } catch (Exception e) {
            }
        }
    }
}
