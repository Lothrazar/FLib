package com.lothrazar.library;
public class VertexFormatTest {
    public static void print() {
        for (java.lang.reflect.Field f : com.mojang.blaze3d.vertex.DefaultVertexFormat.class.getDeclaredFields()) {
            System.out.println(f.getName());
        }
    }
}
