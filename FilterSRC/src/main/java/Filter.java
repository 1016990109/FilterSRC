import java.io.*;

public class Filter {
    private boolean isSRC(File file) {
        return file.getName().equals("src");
    }

    public void filter() {
        File assignmentDirectory = new File("D:/学习/计算系统基础/assignment2");
        File[] submissions = assignmentDirectory.listFiles();

        for (File submission: submissions) {
            if (!submission.isDirectory()) {
                System.out.println("not a directory");
                continue;
            }
            File file = getSRCFile(submission);
            if (file != null) {
                saveSRC(file, submission.getAbsolutePath());
            }
        }
        return;
    }

    private File getSRCFile(File file) {
        if (file.isDirectory()) {
            if (isSRC(file)) {
                return file;
            } else {
                File[] children = file.listFiles();
                for (File child: children) {
                    if (!child.getName().equals("__MACOSX")) {
                        File result = getSRCFile(child);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void saveSRC(File src, String desPath) {
        if (!src.exists()) {
            System.out.println(desPath);
            throw new RuntimeException("no src directory found!");
        }

        copyDirectory(src, desPath);

        //delete other directories
        File[] others = new File(desPath).listFiles();
        for (File file: others) {
            if (!isSRC(file)) {
                if (file.isDirectory()) {
                    deleteDirectory(file.getAbsolutePath());
                } else {
                    deleteFile(file);
                }
            }
        }
    }

    /**
     * delete one file
     * @param file
     * @return
     */
    private static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * delete directory
     * @param path
     * @return
     */
    private boolean deleteDirectory(String path) {
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return flag;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { // delete children
            // Delete file.
            if (file.isFile()) {
                flag = deleteFile(file);
            } else if (file.isDirectory()) {// Delete folder
                flag = deleteDirectory(file.getAbsolutePath());
            }
            if (!flag) { // 只要有一个失败就立刻不再继续
                break;
            }
        }
        flag = dirFile.delete(); // 删除空目录
        return flag;
    }

    private void copyDirectory(File src, String desPath) {
        String newDesPath = desPath + File.separator + src.getName();

        if (src.getAbsolutePath().equals(newDesPath)) {
            System.out.println("des path is the same with src path");
            return;
        }

        if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (File file: files) {
                if (file.isDirectory()) {
                    copyDirectory(file, newDesPath);
                } else {
                    copyFile(file, newDesPath);
                }
            }
        } else {
            return;
        }
    }

    private void copyFile(File file, String desPath) {
        String newDesPath = desPath + File.separator + file.getName();
        if (file.getAbsolutePath().equals(newDesPath)) {
            System.out.println("des path have a file with the same name.");
            return;
        }

        //make dirs
        File destFileDir = new File(desPath);
        destFileDir.mkdirs();

        //copy file
        File desFile = new File(newDesPath);
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(desFile);
            byte[] buf = new byte[1024];
            int c;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Filter().filter();
    }
}
