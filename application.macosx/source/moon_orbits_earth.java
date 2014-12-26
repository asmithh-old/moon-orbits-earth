import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class moon_orbits_earth extends PApplet {

Planet luna;
Planet moon;
Planet Earth;
public void setup()
{
  size(1000,1000);
  luna = new Planet();
  Earth = new Planet();
  moon = new Planet();
}

public void draw()
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
  luna.planetradius = .2725f*Earth.planetradius;
  luna.orbitradius = 300;
  luna.dayside = color(50,0,0);
  luna.nightside = color(200,0,0);
  Earth.omegaspin = 27.32f*luna.omegaspin;
}

class Planet
{
  float planetradius;
  float omegaspin;
  float theta;
  float time;
  float orbitradius;
  int dayside;
  int nightside;
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
    omegaspin=.01f;
    theta = 0;
    time = 0;
    orbitradius = 100;
    xpos_init = 500;
    ypos_init = 500;
    father_time = .01f;
    orbit_centerx = 500;
    orbit_centery = 500;
    
  }

  public void display()
  {   
      beginShape();
      //semicircle, should be dark one
      fill(nightside);
      ellipseMode(CENTER);
      arc(xpos-cos(theta), ypos-sin(theta), planetradius, planetradius, theta + (3.14f/2), theta+(3.14f*1.5f));
      endShape();
      beginShape();
      //semicircle make light
      fill(dayside);
      ellipseMode(CENTER);
      arc(xpos-cos(theta),ypos-sin(theta),planetradius,planetradius, theta+(3.14f*1.5f), theta+2.5f*3.14f);
      endShape();
  }
  public void time_passes()
  {
    time = time + father_time;
  }
  public void orbit()
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
    e = .333333333f;
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
  public void spin()
  {
    //makes obj spin
    //omegaspin is rate of spinning
    theta = theta + omegaspin;
    //rotation about axis to show night side and day side
  }
  public void tidal_bulge()
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
    e = .055f;
    //^^^this is normal orbit of moon
    n = 5;
    E = approxE(x,e,M,n);
    angle = 2*atan(sqrt((1+e)/(1-e))*tan(E/2));
    radius = (orbitradius/(1+e))*(1-e*e)/(1+e*cos(angle));
    force = 100000/(radius*radius);
    beginShape();
    fill(0,0,255);
    arc (xpos , ypos , 2*force+planetradius, 2*force+planetradius,angle-3.14f/4, angle +3.14f/4);
    endShape();
    beginShape();
    fill(0,0,255);
    arc (xpos, ypos, force+planetradius, force+planetradius,angle+3.14f-3.14f/4, angle + 3.14f+3.14f/4);
    endShape();

  }  
  public void Circle()
  {
  xpos = orbit_centerx + orbitradius*cos(theta);
  ypos = orbit_centery + orbitradius*sin(theta);
  }
}


//first-order taylor approximation of solution to kepler equation
//uses recursive iteration or something fancy 
//x is initial guess, e is eccentricity of elliptical orbit, M is time * 2pi/orbital period
//n is number of iterations--we'll go for 3-5
public float approxE(float x,float e,float M,int n)
{
    if (n == 0)
    {
        return x;
    }
    else
    {
        return approxE(x,e,M,n-1)-(approxE(x,e,M,n-1)-e*sin(approxE(x,e,M,n-1))-M)/(1-e*cos(approxE(x,e,M,n-1)));
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "moon_orbits_earth" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
