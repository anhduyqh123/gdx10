/*
 * Original shader from: https://www.shadertoy.com/view/XsffRs
 */

#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

// shadertoy emulation
#define iTime time
#define iResolution resolution

// --------[ Original ShaderToy begins here ]---------- //
#define TAU 6.28318531
float C,S;

mat2 rot(float a){
    return mat2(C=cos(a),S=sin(a),-S,C);
}

float map(vec3 p) {
    p.yz*=rot(p.z*(.03*sin(iTime*3.)));
    p.xz*=rot(p.z*(.03*cos(iTime*3.)));
    float m=TAU/6.,
    l=length(p.xy),
    a=mod(atan(p.y,p.x)-p.z*.5+iTime*5.,m)-.5*m;
    return length(vec2(a*l,l-2.))-.8;
}


void main(void)
{
    vec2 uv = gl_FragCoord.xy / iResolution.xy;
    uv-=.5;
    uv.x*=iResolution.x/iResolution.y;
    vec3 ro=vec3(uv,-3.),rd=normalize(vec3(uv,1.)),mp=ro;
    float i=0.;
    for (int ii=0;ii<30;++ii) {
        i++;
        float md=map(mp);
        if (abs(md)<.001)break;
        mp+=rd*md;
    }
    float r=i/30.;
    float d=length(mp-ro)*.1;
    vec3 c=mix(vec3(.2,.5,.7)*d*d,vec3(.2,.4,.8)*r/d,r*r);
    c=sqrt(c);
    gl_FragColor = vec4(c,1.);

}