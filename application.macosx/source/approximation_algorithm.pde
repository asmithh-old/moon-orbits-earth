//first-order taylor approximation of solution to kepler equation
//uses recursive iteration or something fancy 
//x is initial guess, e is eccentricity of elliptical orbit, M is time * 2pi/orbital period
//n is number of iterations--we'll go for 3-5
float approxE(float x,float e,float M,int n)
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



