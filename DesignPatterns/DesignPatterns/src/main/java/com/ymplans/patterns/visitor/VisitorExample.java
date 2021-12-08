package com.ymplans.patterns.visitor;

/**
 * @author Jos
 */
public class VisitorExample {
    public static void main(String[] args) {
        ResourceFile aFile = new DocxFile();
        ResourceFile bFile = new PdfFile();
        aFile.accept(new Extractor());
        bFile.accept(new Extractor());
    }
}

abstract class ResourceFile {
    public abstract void accept(Extractor extractor);
    public abstract void accept(Compressor compressor);
}

class DocxFile extends ResourceFile {

    @Override
    public void accept(Extractor extractor) {
        extractor.extract2txt(this);
    }

    @Override
    public void accept(Compressor compressor) {
        compressor.compress(this);
    }

}

class PdfFile extends ResourceFile {

    @Override
    public void accept(Extractor extractor) {
        extractor.extract2txt(this);
    }

    @Override
    public void accept(Compressor compressor) {
        compressor.compress(this);
    }
}


class Extractor {
    public void extract2txt(DocxFile docxFile) {
        System.out.println("Docx文件转换成图片");
    }

    public void extract2txt(PdfFile pdfFile) {
        System.out.println("Pdf文件转换成图片");
    }
}


class Compressor{
    public void compress(DocxFile docxFile){
        System.out.println("压缩Docx文件");
    }

    public void compress(PdfFile pdfFile){
        System.out.println("压缩Pdf文件");
    }
}
