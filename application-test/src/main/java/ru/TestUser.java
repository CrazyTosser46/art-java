package ru;

public class TestUser {
    public static void main(String[] args) {
        Test test = new Test();
        test.integer = 123;
        test.string = "���";
        System.out.println(Test.toModel.map(Test.fromModel.map(test)));
    }
}
