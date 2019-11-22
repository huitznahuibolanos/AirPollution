% Library to use csv related functions.
:- use_module(library(csv)).

%Knoledge Base for index Quality.
recomendation('NO2',1,'Los niveles de NO2 son bajos, todo esta bien.').
recomendation('NO2',41,'Los niveles de NO2 son moderados, en caso de padecer algun problema respiratorio usar cubrebocas').
recomendation('NO2',180,'Los niveles de NO2 son altos, es recomendable no salir de casa.').
recomendation('NO2',240,'Los niveles de NO2 son extremadamente altos, salir puede ocasionar severas complicasiones en las vias respiratorias.').
recomendation('O3',1,'Los niveles de O3 son bajos, todo esta bien.').
recomendation('O3',51,'Los niveles de O3 son moderados, se recomienda consumir vitamina C y E para cuidar su salud.').
recomendation('O3',169,'Los niveles de O3 son altos, Usar cubrebocas y salir lo menos posible.').
recomendation('O3',230,'Los niveles de O3 son extremadamente altos, Exposición continua puede ocasionar problemas permanentes de salud.').
recomendation('PM10',1,'Los niveles de PM10 son bajos, todo esta bien.').
recomendation('PM10',41,'Los niveles de PM10 son moderados, en caso de padecer algun problema respiratorio usar cubrebocas').
recomendation('PM10',180,'Los niveles de PM10 son altos, es recomendable no salir de casa.').
recomendation('PM10',240,'Los niveles de PM10 son extremadamente altos, salir puede ocasionar severas complicasiones en las vias respiratorias.').
recomendation('CO',1,'Los niveles de CO son bajos, todo esta bien.').
recomendation('CO',41,'Los niveles de CO son moderados, en caso de padecer algun problema respiratorio usar cubrebocas').
recomendation('CO',180,'Los niveles de CO son altos, es recomendable no salir de casa.').
recomendation('CO',240,'Los niveles de CO son extremadamente altos, salir puede ocasionar severas complicasiones en las vias respiratorias.').
recomendation('SO2',1,'Los niveles de SO2 son bajos, todo esta bien.').
recomendation('SO2',41,'Los niveles de SO2 son moderados, en caso de padecer algun problema respiratorio usar cubrebocas').
recomendation('SO2',180,'Los niveles de SO2 son altos, es recomendable no salir de casa.').
recomendation('SO2',240,'Los niveles de SO2 son extremadamente altos, salir puede ocasionar severas complicasiones en las vias respiratorias.').

greaterNO(Conc,Res):-
	Res is 240,
	Res < Conc.

greaterNO(Conc,Res):-
	Res is 180,
	Res < Conc.

greaterNO(Conc,Res):-
	Res is 41,
	Res < Conc.

greaterNO(_,Res):-
	Res is 1.

greaterO3(Conc,Res):-
	Res is 240,
	Res < Conc.

greaterO3(Conc,Res):-
	Res is 180,
	Res < Conc.

greaterO3(Conc,Res):-
	Res is 41,
	Res < Conc.

greaterO3(_,Res):-
	Res is 1.

% funtion which imports data from ProcessedData.csv, and adds it to the KB.
% Format:
% molecule(molecule,zone,year,concentration_value).
import:-
    csv_read_file('src/Data/ProcessedData.csv', Data, [functor(molecule), arity(4)]),
    maplist(assert, Data).

% Call to the import function to execute after load.
:- import.
% Function that evaluates NO2 molecules and prints recomendation.
evaluateNO2(Mol,Conc):-
	greaterNO(Conc,Res),
	recomendation(Mol,Res,Rec),
	print(Rec).

% Function that evaluates NO2 molecules and prints recomendation.
evaluateO3(Mol,Conc):-
	greaterO3(Conc,Res),
	recomendation(Mol,Res,Rec),
	print(Rec).

% Function that evaluates if there is a need 
evaluateZoneYear(Zone, Year):-
	molecule('NO2',Zone,Year,ConcNO2),
	evaluateNO2('NO2',ConcNO2),
	nl(),
	molecule('PM10',Zone,Year,ConcPM10),
	evaluateNO2('PM10',ConcPM10),
	nl(),
	molecule('CO',Zone,Year,ConcCO),
	evaluateNO2('CO',ConcCO),
	nl(),
	molecule('SO2',Zone,Year,ConcSO2),
	evaluateNO2('SO2',ConcSO2),
	nl(),
	molecule('O3',Zone,Year,ConcO3),
	evaluateO3('O3',ConcO3).

% Execute analysis.
:- evaluateZoneYear(zona0,2014).

% Copyright 2019 Huitznahui Bolaños
%This program is free software: you can redistribute it and or modify
%   it under the terms of the GNU General Public License as published by
%    the Free Software Foundation, either version 3 of the License, or
%    (at your option) any later version.
%
%    This program is distributed in the hope that it will be useful,
%    but WITHOUT ANY WARRANTY; without even the implied warranty of
%    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
%    GNU General Public License for more details.
%
%    You should have received a copy of the GNU General Public License
%    along with this program.  If not, see <http://www.gnu.org/licenses/>.
