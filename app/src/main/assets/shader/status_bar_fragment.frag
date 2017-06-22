precision mediump float;
uniform float uStatus;
uniform vec2 uPosition;
uniform vec2 uDimensions;

varying vec4 vPosition;


void main() {
    gl_FragColor = vec4(0.3, 0.3, 0.3, 0.6);

    float minX = uPosition.x - uDimensions.x / 2.0;
    float maxX = minX + uDimensions.x;
    float valueX = (maxX - vPosition.x) / uDimensions.x;

    if (abs(maxX - vPosition.x) < 0.01) {
        gl_FragColor = vec4(0.1, 0.1, 0.1, 0.6);
        return;
    }

    if (abs(minX - vPosition.x) < 0.01) {
        gl_FragColor = vec4(0.1, 0.1, 0.1, 0.6);
        return;
    }

    float minY = uPosition.y - uDimensions.y / 2.0;
    float maxY = minY + uDimensions.y;

    if (abs(maxY - vPosition.y) < 0.01) {
        gl_FragColor = vec4(0.1, 0.1, 0.1, 0.6);
        return;
    }

    if (abs(minY - vPosition.y) < 0.01) {
        gl_FragColor = vec4(0.1, 0.1, 0.1, 0.6);
        return;
    }

    if (valueX < uStatus)
        gl_FragColor = vec4(0.8, 0.8, 0.3, 0.8);
}