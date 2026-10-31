#version 330 core

precision lowp float;

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;
uniform float uIntensity;

void main() {
    vec4 bloom = texture(uTexture, uv);
    color = vec4(bloom.rgb * uIntensity, bloom.a);
}
