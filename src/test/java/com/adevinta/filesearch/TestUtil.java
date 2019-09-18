package com.adevinta.filesearch;

import com.adevinta.filesearch.dto.FileResult;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestUtil {
    public static List<FileResult> getTestIndexedFiles() {
        FileResult result1 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test1.txt", "This is just a dummy content to test the core functionality of the application and perhaps demonstrate console testing.");
        FileResult result2 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test2.txt", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        FileResult result3 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test3.txt", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
        FileResult result4 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test4.txt", "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.");
        FileResult result5 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test5.txt", "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, to demonstrate and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        FileResult result6 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test6.txt", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.");
        FileResult result7 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test7.txt", "Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).");
        FileResult result8 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test8.txt", "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.");
        FileResult result9 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test9.txt", "If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text.");
        FileResult result10 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test10.txt", "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. ");
        FileResult result11 = new FileResult(0, BigDecimal.ONE, "/usr/path/test/test11.txt", "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. ");

        return Arrays.asList(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11);
    }

    public static List<FileResult> getTestResultList() {
        FileResult result1 = new FileResult(90, BigDecimal.ONE, "/usr/path/test/test1.txt", "This is just a dummy content to test the core functionality of the application and perhaps demonstrate console testing.");
        FileResult result2 = new FileResult(80, BigDecimal.ONE, "/usr/path/test/test2.txt", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        FileResult result3 = new FileResult(70, BigDecimal.ONE, "/usr/path/test/test3.txt", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
        FileResult result4 = new FileResult(60, BigDecimal.ONE, "/usr/path/test/test4.txt", "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.");
        FileResult result5 = new FileResult(50, BigDecimal.ONE, "/usr/path/test/test5.txt", "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, to demonstrate and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        FileResult result6 = new FileResult(40, BigDecimal.ONE, "/usr/path/test/test6.txt", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.");
        FileResult result7 = new FileResult(30, BigDecimal.ONE, "/usr/path/test/test7.txt", "Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).");
        FileResult result8 = new FileResult(20, BigDecimal.ONE, "/usr/path/test/test8.txt", "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.");
        FileResult result9 = new FileResult(10, BigDecimal.ONE, "/usr/path/test/test9.txt", "If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text.");
        FileResult result10 = new FileResult(5, BigDecimal.ONE, "/usr/path/test/test10.txt", "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. ");
        FileResult result11 = new FileResult(5, BigDecimal.ONE, "/usr/path/test/test11.txt", "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. ");

        return Arrays.asList(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11);
    }
}
