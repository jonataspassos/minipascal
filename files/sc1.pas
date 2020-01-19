program sc1;
var y : integer;
var x,z : real;
var b: boolean;
var p: array[10~10] of array[10~10] of boolean;
begin
	x := 1.0;
	y := 2;
	z := x+y*45*(30+45);
	if x=y then
	begin
		x := 3*y*5+1*2*y+3*x*4.0;
		x := (y-x)/2;
		p[y][y] := (x=y) or (z=x);
	end
	else
		b := (x = y) or (y = x);
end.