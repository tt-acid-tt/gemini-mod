#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform float time;
uniform float wetFloorActive;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    if (wetFloorActive > 0.5) {
        vec2 uv = texCoord;
        if (uv.y > 0.5) {
            vec2 reflectedUV = vec2(
                uv.x + sin(uv.y * 25.0 + time * 3.0) * 0.004,
                1.0 - uv.y + sin(uv.x * 25.0 + time * 2.0) * 0.004
            );
            vec4 refColor = texture(DiffuseSampler, reflectedUV);
            color = mix(color, refColor * vec4(0.8, 0.9, 1.1, 1.0), 0.45);
        }
    }
    fragColor = color;
}
