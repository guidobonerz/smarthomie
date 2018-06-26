// berechne Taupunkt durch Temperatur und relativer Luftfeuchte
function getDewPoint(temperature, relativeHumidity)
{
	var vp = getVaporPressure(temperature, relativeHumidity);
	var a, b;

	if(temperature >= 0)
	{
		a = 7.5;
		b = 237.3;
	}
	else
	{
		a = 7.6;
		b = 240.7;
	}
	
	var c = Math.log(vp/6.1078) * Math.LOG10E;

	var dp = (b * c) / (a - c);

	return round(dp, 1);
}

// Dampfdruck in Abhängigkeit von der Temperatur und der relativen Feuchte
function getVaporPressure(temperature, relativeHumidity)
{
	var svp = getSaturationVaporPressure(temperature);
	var vp = relativeHumidity/100 * svp;

	return vp;
}

// Sättingungsdampfdruck in Abhängigkeit von der Temperatur
function getSaturationVaporPressure(temperature)
{
	var a, b;
	if(temperature >= 0)	// Sättigungsdampfdruck über Wasser
	{
		a = 7.5;
		b = 237.3;
	}
	else
	{
		a = 7.6;
		b = 240.7;
	}

	var svp = 6.1078 * Math.exp(((a*temperature)/(b+temperature))/Math.LOG10E);

	return svp;
}

function round(x, n)
{
	n = Math.pow(10, n);
	x = Math.round(x*n);
	return x/n;
}