function [ intersecPixels ] = getIntersection( im,se,f,c )
    nhood=getnhood(se);
    [filas,columnas]=size(nhood);
    s=size(im);
    %centro de se
    cYSE=uint8(filas/2);
    cXSE=uint8(columnas/2);
    
    %
    distanciaOrigenFilas=cYSE-1;
    distanciaOrigenColumnas=cXSE-1;
    distanciaFinFilas=uint8(filas-cYSE);
    distanciaFinColumnas=uint8(columnas-cXSE);
    
    startF=max(f-double(distanciaOrigenFilas),1);
    startC=max(c-double(distanciaOrigenColumnas),1);
    endF=double(min(f+double(distanciaFinFilas),s(1)));
    endC=double(min(c+double(distanciaFinColumnas),s(2)));
    
    pixels=[];
    
    for fil=startF:endF
        for col=startC:endC
            i=cYSE-(f-fil);
            j=cXSE-(c-col);
           %startC,startF,cXSE,cYSE,f,c,fil,col,i,j
            if nhood(i,j)==1
              pixels=[pixels,im(fil,col,:)] ;
            end;   
        end;
    end;
    intersecPixels=pixels;
end

