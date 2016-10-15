function [ globalR ] = getGlobalR( r )
    global R0;
    global R1;
    global R2;
    
    if r==0
        globalR=R0;
    elseif r==1
        globalR=R1;
    elseif r==2
        globalR=R2;
    else
        globalR=R0;
    end;
end

