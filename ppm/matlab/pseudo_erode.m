function [ out ] = pseudo_erode( im, se )
    [fila,col,deep]=size(im);
    out=zeros(fila,col,3);
    for f=1:fila
        for c=1:col
            intersec=getIntersection(im,se,f,c);
            erodedPixel=pseudo_erode0(intersec);
            out(f,c,1)=erodedPixel(1);
            out(f,c,2)=erodedPixel(2);
            out(f,c,3)=erodedPixel(3);
            %break;
        end;
        %break;
    end;


end

