function [ proyect ] = proyectar( A,B )
    proyect=(dot(A,B)/norm(B)^2)*B;

end

