attribute vec4 aVertexPosition;

uniform mat4 uMVPMatrix;

varying vec4 vPosition;

void main() {
    vPosition = aVertexPosition;
    gl_Position = uMVPMatrix * aVertexPosition;
}