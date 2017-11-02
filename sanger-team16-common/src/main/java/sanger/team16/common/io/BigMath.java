/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2010  Genome Research Ltd.
 *
 *  Genevar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sanger.team16.common.io;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigMath
{
    /*
     * http://java.sun.com/javase/6/docs/api/java/math/BigDecimal.html#BigDecimal%28double%29
     * http://mindprod.com/jgloss/round.html
     */
    public static double round(double v, int scale) {
        BigDecimal bd = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        
        return bd.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public static String scientificNotation(double number) {
        if (number >= 0.001)
            return new Double(number).toString();
        else
            return new DecimalFormat("0.#########E0").format(number);
    }
}
