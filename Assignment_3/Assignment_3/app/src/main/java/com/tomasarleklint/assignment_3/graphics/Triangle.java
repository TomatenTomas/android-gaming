package com.tomasarleklint.assignment_3.graphics;

import com.tomasarleklint.assignment_3.graphics.Mesh;

public class Triangle extends Mesh {
    static float vertices[] = { // in counterclockwise order:
            0.0f,  0.5f, 0.0f, 	// top
            -0.5f, -0.5f, 0.0f,	// bottom left
            0.5f, -0.5f, 0.0f  	// bottom right
    };
    public Triangle() {
        super(vertices);
    }
}