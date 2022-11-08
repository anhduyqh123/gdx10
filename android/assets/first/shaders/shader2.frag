#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_tex0;

void main() {
    vec2 uv = v_texCoords;
    vec4 color0 = texture2D(u_tex0,uv);
    vec4 color1 = texture2D(u_texture,uv);
    gl_FragColor = color1*vec4(1,1,1,0)+color0*vec4(0,0,0,1);
}