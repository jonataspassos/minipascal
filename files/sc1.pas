program sc1;
var y : integer;
var x,z : real;
var b: boolean;
begin
	x := 1.0;
	y := 2;
	z := x+y*45*(30+45);
	if x=y then
	begin
		x := 3*4*5+0.1*0.2*0.3+3*0.5*4.0;
		x := (y-x)/2;
		b := (x=y) or (z=x);
	end
	else
		b := (x = z) or (y = x);
end.