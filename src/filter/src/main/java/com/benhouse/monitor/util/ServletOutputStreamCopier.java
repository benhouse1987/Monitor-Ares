package com.benhouse.monitor.util;

import javax.servlet.ServletOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yunfeima on 2017/6/7.
 */
public class ServletOutputStreamCopier extends ServletOutputStream {

    private OutputStream outputStream;
    private ByteArrayOutputStream copy;

    public ServletOutputStreamCopier(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream(1024);
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        copy.write(b);

    }


    public byte[] getCopy() {
        return copy.toByteArray();
    }

}