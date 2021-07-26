package Extend.Archive;

import GameGDX.Assets;
import GameGDX.GDX;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileHandle {
    private Map<String, InputStream> map = new HashMap<>();

    public ZipFileHandle(String name)
    {
        InitStream(GDX.Decode(GDX.GetFile(name)));
    }
    public InputStream GetInputStream(String url)
    {
        try {
            map.get(url).reset();
            return map.get(url);
        } catch (Exception e) {
            throw new GdxRuntimeException("File not found: " + url + " (Archive2)");
        }
    }
    private void InitStream(byte[] bytes) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ZipInputStream zipIsZ = new ZipInputStream(byteArrayInputStream);
            try {
                ZipEntry entry;
                while ((entry = zipIsZ.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String name = entry.getName();
                        map.put(name, CloneInputStream(zipIsZ));
                    }
                }
            }
            finally {
                zipIsZ.close();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private InputStream CloneInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyLarge(in, out); //IOUtils.copyLarge(in, out);
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        return is;
    }

    private long copyLarge(InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[input.available()];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
