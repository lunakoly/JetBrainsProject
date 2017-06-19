attribute vec4 aVertexPosition;

varying vec4 vPosition;

void main() {
    vPosition = aVertexPosition;
    gl_Position = aVertexPosition;
}