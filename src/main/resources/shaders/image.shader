#shader vert
#version 120

void main(void) {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}

#shader frag

// uniform sampler2D sampler;
uniform sampler2D overlaySampler;
uniform sampler2D sampler;
uniform float imageX;
uniform float imageY;
uniform float imageWidth;
uniform float imageHeight;
uniform float mixFactor;
uniform float alpha;

// uniform vec4 imageDimensions;
// uniform vec2 dimensions;

void main() {
    // vec2 coords1 = vec2(gl_FragCoord.x / imageDimensions.z, gl_FragCoord.y / imageDimensions.w);
    if (gl_FragCoord.x >= imageX && gl_FragCoord.y >= imageY) {
        vec2 coords = vec2(((gl_FragCoord.x - imageX) / imageWidth), ((gl_FragCoord.y - imageY) / imageHeight));
        vec4 color = mix(texture(sampler, gl_TexCoord[0].xy), texture(overlaySampler, coords), mixFactor);
        color.a = alpha;
        gl_FragColor = color;
    } else {
        gl_FragColor = vec4(1, 1, 1, 1);
    }
    /*if (gl_FragCoord.x >= floor(imageDimensions.x) && gl_FragCoord.y >= floor(imageDimensions.y)) {
        gl_FragColor = texture(overlaySampler, coords);
    } else {
        gl_FragColor = vec4(1, 1, 1, 1); // this won't get rendered anyway, just compiler bait :P
    }*/


    // gl_FragColor = texture(overlaySampler, vec2(gl_FragCoord.x / dimensions.x, gl_FragCoord.y / dimensions.y));

}