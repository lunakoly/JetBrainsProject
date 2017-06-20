attribute vec4 aVertexPosition;
attribute vec2 aTextureCoord;

varying vec2 vTextureCoord;
varying vec4 vPosition;

void main() {
    vTextureCoord = aTextureCoord;
    gl_Position = aVertexPosition;
    vPosition = gl_Position;
}