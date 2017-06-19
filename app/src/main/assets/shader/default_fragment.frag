precision mediump float;

varying vec4 vPosition;

void main() {
    vec4 color = vec4(0.0, 0.0, 0.0, 1.0);

    if (mod(vPosition.y, 0.2) >= 0.1 &&
        mod(vPosition.x, 0.2) < 0.1 ||
        mod(vPosition.y, 0.2) < 0.1 &&
        mod(vPosition.x, 0.2) >= 0.1) {
        color.r = 1.0;
        color.b = 1.0;
    }

    gl_FragColor = color;

}