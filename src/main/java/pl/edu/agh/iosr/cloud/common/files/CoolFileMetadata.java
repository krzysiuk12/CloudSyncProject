package pl.edu.agh.iosr.cloud.common.files;

import org.joda.time.DateTime;

public class CoolFileMetadata {

    private final CoolCloudPath path;
    private final FileType type;
    private final String fileName;
    private final String extension;
    private final long size;
    private final DateTime lastModificationTime;

    CoolFileMetadata(CoolCloudPath path, FileType type, String fileName, String extension, long size, DateTime lastModificationTime) {
        this.path = path;
        this.type = type;
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.lastModificationTime = lastModificationTime;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public CoolCloudPath getPath() {
        return path;
    }

    public FileType getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public long getSize() {
        return size;
    }

    public DateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public static class Builder {
        private CoolCloudPath path;
        private FileType type;
        private String fileName;
        private String extension;
        private long size;
        private DateTime lastModificationTime;

        public Builder setPath(CoolCloudPath path) {
            this.path = path;
            return this;
        }

        public Builder setType(FileType type) {
            this.type = type;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setExtension(String extension) {
            this.extension = extension;
            return this;
        }

        public Builder setSize(long size) {
            this.size = size;
            return this;
        }

        public Builder setLastModificationTime(DateTime lastModificationTime) {
            this.lastModificationTime = lastModificationTime;
            return this;
        }

        public CoolFileMetadata build() {
            return new CoolFileMetadata(path, type, fileName, extension, size, lastModificationTime);
        }
    }
}
