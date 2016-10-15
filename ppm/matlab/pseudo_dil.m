function [ out ] = pseudo_dil( im, se )
    [fila,col,deep]=size(im);
    out=zeros(fila,col,3);
    for f=1:fila
        for c=1:col
            intersec=getIntersection(im,se,f,c);
            dilPixel=pseudo_dil0(intersec);
            out(f,c,1)=dilPixel(1);
            out(f,c,2)=dilPixel(2);
            out(f,c,3)=dilPixel(3);
            
        end;
    end;


end

