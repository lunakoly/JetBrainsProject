precision mediump float;
uniform sampler2D uTextureSampler;

varying vec2 vTextureCoord;
varying vec4 vPosition;


void main() {
    gl_FragColor = texture2D(uTextureSampler, vTextureCoord);
}