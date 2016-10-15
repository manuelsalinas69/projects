function [ exMin,exMax ] = ordenarExtremos( alfa,beta )
    exMin=alfa;
    exMax=beta;

    for i=0:2 
        r=double(getGlobalR(i));
        vR=r(:,2)'-r(:,1)';
        vAB=beta-alfa;
        if dot(vAB,vR)==0
           continue;
        end;
        
        vA=double(alfa)-r(:,1)';
        vB=double(beta)-r(:,1)';
        prodA=dot(vA,vR);
        prodB=dot(vB,vR);

      
        if prodA>prodB
            exMax=alfa;
            exMin=beta;
            
        else
            exMax=beta;
            exMin=alfa;
        end;
        break;
    end;
        
end

