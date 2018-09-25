#version 330 core

uniform vec2 positionOffset;
uniform float angle;
uniform vec3 color;

in vec2 position;

out vec3 inColor;

mat2 rotationMatrix2D(float angle)
{
    return mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
}

void main()
{
    gl_Position = vec4(rotationMatrix2D(angle)*position + positionOffset, 0.0, 1.0);
    inColor = color;
}