function [ r0,r1,r2 ] = getReferences( im )
    k=getGlobalK;
    img=double(im);
    vector=im2Vector(img);
    [r,c,latent,tsquared,explained,mu1]=pca(vector);
    
    [r0min,r0max]=getReference0(c(:,1),k,1);
    [r1min,r1max]=getReference0(c(:,2),k,2);
    [r2min,r2max]=getReference0(c(:,3),k,3);
    r0min = r0min*r' + repmat(mu1,1,1);
    r0max = r0max*r' + repmat(mu1,1,1);
   
    r1min = r1min*r' + repmat(mu1,1,1);
    r1max = r1max*r' + repmat(mu1,1,1);
    
    r2min = r2min*r' + repmat(mu1,1,1);
    r2max = r2max*r' + repmat(mu1,1,1);
    
    r0=uint8([r0min' r0max']);
    r1=uint8([r1min' r1max']);
    r2=uint8([r2min' r2max']);
    
%      r0=([r0min' r0max']);
%     r1=([r1min' r1max']);
%     r2=([r2min' r2max']);

end

