precision mediump float;
uniform sampler2D uTextureSampler;
uniform float uGlobalTime;

varying vec2 vTextureCoord;
varying vec4 vPosition;


void main() {
    gl_FragColor = texture2D(uTextureSampler, vTextureCoord);
    float k = 1.0 - sin(uGlobalTime / 1000.0 * 3.14159265);
    gl_FragColor.r = gl_FragColor.r + 0.1 * k;
    gl_FragColor.g = gl_FragColor.g + 0.1 * k;
}