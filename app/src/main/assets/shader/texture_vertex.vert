attribute vec4 aVertexPosition;
attribute vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform vec2 uDimensions;
uniform vec2 uScreen;
uniform vec2 uPosition;

varying vec2 vTextureCoord;
varying vec4 vPosition;


float scale(float f) {
    return f / 2.0 + 0.5;
}

void main() {
    vec2 tc = aTextureCoord;
    tc.y = tc.y * 2.0 / uDimensions.y;
    tc.x = tc.x * 2.0 / uDimensions.x;

    tc.x = 1.0 - scale(tc.x);
    tc.y = 1.0 - scale(tc.y);

    tc.y = tc.y + uPosition.y / uDimensions.y;
    tc.x = tc.x + uPosition.x / uDimensions.x;

    vTextureCoord = tc;
    gl_Position = uMVPMatrix * aVertexPosition;
    vPosition = gl_Position;
}