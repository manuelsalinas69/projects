function [ r_min,r_max ] = getReference0( range,k,pos)
    BW_axis=[1 1 1];
    [alfa,beta]=chebyshev(range,k);
     r0_alfa=[0,0,0];
    r0_beta=[0,0,0];
    r0_alfa(1,pos)=alfa;
    r0_beta(1,pos)=beta;
     %vector AB
    r0_AB=r0_beta-r0_alfa;
  
    if dot(r0_AB,BW_axis)>=0 
        r_min=r0_alfa;
        r_max=r0_beta;
    else
        r_min=r0_beta;
        r_max=r0_alfa;
    end;

end

