
var file;
var time_checkpoint;
var frames;
var frames_state = [];
var canvas_size = 800;
var world_size = 2;


function preload() {
    file = loadStrings('output.txt');
}

function setup() {
    frames = parseInt(file.length);
    createCanvas(canvas_size, canvas_size);

    readFrames(frames);

    time_checkpoint = millis();
}

readFrames = () => {
    for (let i = 0; i < frames; i++) {
        frames_state.push(getParticlesFromFrame(file[i].trim()))
    }
};

getParticlesFromFrame = frame => {
    frame = frame.split(" ");
    particles = [];
    for (let i = 0; i < frame.length; i += 2) {
        particles.push({x: parseFloat(frame[i]), y: -parseFloat(frame[i+1])}) // y axis inverted
    }
    return particles
};

var current_frame = 0;
var frame_skipping = 0; // amount of frames to skip
function draw() {
    background(24);

    drawParticles(current_frame);

    current_frame = (current_frame + (1 + frame_skipping)) % frames;

    var fps = frameRate();
    fill(255);
    stroke(0);
    text("FPS: " + fps.toFixed(2), 10, height - 10);
}

drawParticle = particle => {
    // console.log(particle)
    var c = color(255, 0, 0);
    fill(c);
    ellipse(world2canvas(particle.x), world2canvas(particle.y), 20);
};

drawParticles = frame => {
    frames_state[frame].forEach(p => {
        drawParticle(p)
    })
};

function world2canvas(value) {
    return (value / world_size) * canvas_size + canvas_size / 2.0;
}