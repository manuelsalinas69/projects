function [ mat ] = im2Vector( im )

    r=im(:,:,1);
    g=im(:,:,2);
    b=im(:,:,3);
    
    r=reshape(r,[],1);
    g=reshape(g,[],1);
    b=reshape(b,[],1);
    
    mat=[r g b];


end

