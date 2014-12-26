Planet luna;
Planet moon;
Planet Earth;
void setup()
{
  size(1000,1000);
  luna = new Planet();
  Earth = new Planet();
  moon = new Planet();
}

void draw()
//this makes the thingies appear
{ background(255);
  luna.display();
  luna.spin();
  luna.orbit();
  moon.display();
  moon.spin();
  moon.Circle();
  luna.time_passes();
  Earth.time_passes();
  Earth.tidal_bulge();
  Earth.display();
  Earth.spin();
  moon.nightside = color(0,200,0);
  moon.dayside = color(0,50,0);
  moon.orbitradius = 300;
  moon.planetradius = luna.planetradius;
  Earth.planetradius = 50;
  luna.planetradius = .2725*Earth.planetradius;
  luna.orbitradius = 300;
  luna.dayside = color(50,0,0);
  luna.nightside = color(200,0,0);
  Earth.omegaspin = 27.32*luna.omegaspin;
}

class Planet
{
  float planetradius;
  float omegaspin;
  float theta;
  float time;
  float orbitradius;
  color dayside;
  color nightside;
  float xpos;
  float ypos;
  float xpos_init;
  float ypos_init;
  float xinc;
  float father_time;
  float orbit_centerx;
  float orbit_centery;

  Planet()
  {
    dayside = color(255,255,0);
    nightside = color(30);
    xpos = 500;
    ypos = 500;
    //xpos and ypos are location of *center* of objects
    planetradius = 8;
    omegaspin=.01;
    theta = 0;
    time = 0;
    orbitradius = 100;
    xpos_init = 500;
    ypos_init = 500;
    father_time = .01;
    orbit_centerx = 500;
    orbit_centery = 500;
    
  }

  void display()
  {   
      beginShape();
      //semicircle, should be dark one
      fill(nightside);
      ellipseMode(CENTER);
      arc(xpos-cos(theta), ypos-sin(theta), planetradius, planetradius, theta + (3.14/2), theta+(3.14*1.5));
      endShape();
      beginShape();
      //semicircle make light
      fill(dayside);
      ellipseMode(CENTER);
      arc(xpos-cos(theta),ypos-sin(theta),planetradius,planetradius, theta+(3.14*1.5), theta+2.5*3.14);
      endShape();
  }
  void time_passes()
  {
    time = time + father_time;
  }
  void orbit()
  {
    float x;
    //first guess for angle theta, the true anomaly (the theta that's useful for polar coord)
    float e;
    //eccentricity of ellipse
    float M;
    //mean anomaly.  is 2pi/orbital period * time
    int n;
    //number of guess iterations
    float radius;
    //something this function will spit out 
    float angle;
    //eventually what we decide the angle theta for polar coord should be
    float E;
    //eccentric anomaly
    float axis;
    //semi-major axis
    M = time;
    x = M;
    e = .333333333;
    //^^^ this is for altered orbit once moon is hit w/asteroid
    //e = .055;
    //^^^this is normal orbit of moon
    n = 5;
    E = approxE(x,e,M,n);
    angle = 2*atan(sqrt((1+e)/(1-e))*tan(E/2));
    radius = (orbitradius/(1+e))*(1-e*e)/(1+e*cos(angle));
    xpos = orbit_centerx + radius * cos(angle);
    ypos = orbit_centery + radius * sin(angle);
      }
  void spin()
  {
    //makes obj spin
    //omegaspin is rate of spinning
    theta = theta + omegaspin;
    //rotation about axis to show night side and day side
  }
  void tidal_bulge()
  {
    float x;
    //first guess for angle theta, the true anomaly (the theta that's useful for polar coord)
    float e;
    //eccentricity of ellipse
    float M;
    //mean anomaly.  is 2pi/orbital period * time
    int n;
    //number of guess iterations
    float radius;
    //something this function will spit out 
    float angle;
    //eventually what we decide the angle theta for polar coord should be
    float E;
    //eccentric anomaly
    float axis;
    //semi-major axis
    float force;
    M = time;
    x = M;
    //e = .333333333;
    //^^^ this is for altered orbit once moon is hit w/asteroid
    e = .055;
    //^^^this is normal orbit of moon
    n = 5;
    E = approxE(x,e,M,n);
    angle = 2*atan(sqrt((1+e)/(1-e))*tan(E/2));
    radius = (orbitradius/(1+e))*(1-e*e)/(1+e*cos(angle));
    force = 100000/(radius*radius);
    beginShape();
    fill(0,0,255);
    arc (xpos , ypos , 2*force+planetradius, 2*force+planetradius,angle-3.14/4, angle +3.14/4);
    endShape();
    beginShape();
    fill(0,0,255);
    arc (xpos, ypos, force+planetradius, force+planetradius,angle+3.14-3.14/4, angle + 3.14+3.14/4);
    endShape();

  }  
  void Circle()
  {
  xpos = orbit_centerx + orbitradius*cos(theta);
  ypos = orbit_centery + orbitradius*sin(theta);
  }
}


