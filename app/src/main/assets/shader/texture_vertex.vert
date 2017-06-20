attribute vec4 aVertexPosition;
attribute vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform vec2 uDimensions;
uniform vec2 uScreen;

varying vec2 vTextureCoord;
varying vec4 vPosition;


float scale(float f) {
    return f / 2.0 + 0.5;
}

void main() {
    vec2 tc = aTextureCoord;
    tc.x = tc.x * uDimensions.y / uDimensions.x;

    tc.x = 1.0 - scale(tc.x);
    tc.y = 1.0 - scale(tc.y);

    vTextureCoord = tc;
    gl_Position = uMVPMatrix * aVertexPosition;
    vPosition = gl_Position;
}