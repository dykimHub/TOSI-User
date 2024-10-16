package com.tosi.user.controller;

public class ExampleObject {
    public static final String join = """
            {
              "email": "test@test.com",
              "password": "test",
              "nickname": "test",
              "bookshelfName": "testshelf",
              "children": [
                {
                  "childId": 0,
                  "childName": "경수",
                  "childGender": 0
                }
              ]
            }
            """;

    public static final String login = """
            {
              "email": "test@test.com",
              "password": "test"
            }
            """;

    public static final String child = """
            {
              "childId": 0,
              "childName": "영지",
              "childGender": 1
            }
            """;

    public static final String modify = """
            {
              "nickname": "tosi",
              "bookshelfName": "tosishelf"
            }
            """;

}