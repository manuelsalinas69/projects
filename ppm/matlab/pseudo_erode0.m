function [ erodedPixel ] = pseudo_erode0( pIntersect )
    k=getGlobalK;
    vec=im2Vector(pIntersect);
    [r,c,latent,tsquared,explained,mu1]=pca(vec);
    [alfa_pca,beta_pca]=chebyshev(c(:,1),k);
    pA_pca=[alfa_pca 0 0];
    pB_pca=[beta_pca 0 0];
    
    pA=pA_pca*r' + repmat(mu1,1,1);
    pB=pB_pca*r' + repmat(mu1,1,1);
    [pMin,pMax]=ordenarExtremos(pA,pB);
    erodedPixel=uint8(pMin);
    


end

