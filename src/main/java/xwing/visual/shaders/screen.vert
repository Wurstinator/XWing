#version 330 core

uniform float time;

in vec2 position;
in vec3 color;

out vec3 inColor;

mat2 rotationMatrix2D(float angle)
{
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;

    return mat2(c, -s, s,  c);
}

vec3 shift(vec3 v, int shift)
{
    shift = shift % 3;
    if (shift == 0)
        return v;
    if (shift == 1)
        return vec3(v.z, v.x, v.y);
    return vec3(v.y, v.z, v.x);
}

void main()
{
    float pi = 3.141;
    gl_Position = vec4(rotationMatrix2D(pi * 2 * time) * position, 0.0, 1.0);
    int shiftv = int(floor(time*3)) + 1;
    float mixt = mod(time*3, 1);

    vec3 shiftc1 = shift(color, shiftv);
    vec3 shiftc2 = shift(color, shiftv+1);
    inColor = mix(shiftc1, shiftc2, mixt);
}