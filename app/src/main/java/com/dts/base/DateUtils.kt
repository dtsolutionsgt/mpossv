package com.dts.base

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class DateUtils {

    fun sfecha(f: Long): String {
        var f = f
        val vy: Long
        val vm: Long
        val vd: Long
        var s: String
        vy = f / 100000000
        f = f % 100000000
        vm = f / 1000000
        f = f % 1000000
        vd = f / 10000
        f = f % 10000
        s = ""
        s = if (vd > 9) "$s$vd/" else s + "0" + vd.toString() + "/"
        s = if (vm > 9) "$s$vm/20" else s + "0" + vm.toString() + "/20"
        s = if (vy > 9) s + vy.toString() else s + "0" + vy.toString()
        return s
    }

    fun sfechas(f: Long): String {
        var f = f
        val vy: Int
        val vm: Int
        val vd: Int
        var s: String
        vy = f.toInt() / 100000000
        f = f % 100000000
        vm = f.toInt() / 1000000
        f = f % 1000000
        vd = f.toInt() / 10000
        f = f % 10000
        s = ""
        s = if (vd > 9) {
            "$s$vd/"
        } else {
            s + "0" + vd.toString() + "/"
        }
        s = if (vm > 9) {
            "$s$vm/"
        } else {
            s + "0" + vm.toString() + "/"
        }
        s = if (vy > 9) {
            s + vy.toString()
        } else {
            s + "0" + vy.toString()
        }
        return s
    }

    fun sfechash(f: Long): String {
        var f = f
        val vy: Int
        val vm: Int
        val vd: Int
        var s: String
        vy = f.toInt() / 100000000
        f = f % 100000000
        vm = f.toInt() / 1000000
        f = f % 1000000
        vd = f.toInt() / 10000
        f = f % 10000
        s = ""
        s = if (vd > 9) {
            "$s$vd/"
        } else {
            s + "0" + vd.toString() + "/"
        }
        s = if (vm > 9) {
            s + vm.toString()
        } else {
            s + "0" + vm.toString()
        }
        return s
    }

    fun shora(vValue: Long): String {
        var h: Long
        val m: Long
        val sh: String
        val sm: String
        if (vValue == 0L) return ""
        h = vValue % 10000
        m = h % 100
        sm = if (m > 9) {
            m.toString()
        } else {
            "0$m"
        }
        h = (h.toInt() / 100).toLong()
        sh = if (h > 9) {
            h.toString()
        } else {
            "0$h"
        }
        return "$sh:$sm"
    }

    fun shorasp(vValue: Long): String {
        var h: Long
        val m: Long
        val sh: String
        val sm: String
        if (vValue == 0L) return "     "
        h = vValue % 10000
        m = h % 100
        sm = if (m > 9) {
            m.toString()
        } else {
            "0$m"
        }
        h = (h.toInt() / 100).toLong()
        sh = if (h > 9) {
            h.toString()
        } else {
            "0$h"
        }
        return "$sh:$sm"
    }

    fun sfechalocal(f: Long): String {
        if (f == 0L) return ""
        var s = sfecha(f)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(s)
            s = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
        } catch (e: Exception) {
            s = ""
        }
        return s
    }

    fun sfechalocalmonth(f: Long): String {
        var f = f
        if (f == 0L) return ""
        val s = monthname(f)
        f = getyear(f).toLong()
        return "$s $f"
    }

    fun sfechalocalshort(f: Long): String {
        if (f == 0L) return ""
        var s = sfecha(f)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(s)
            s = DateFormat.getDateInstance(DateFormat.SHORT).format(date)
            s = s.substring(0, s.length - 3)
        } catch (e: Exception) {
            s = ""
        }
        return s
    }

    fun sfecham(f: Long): String {
        var f = f
        val vy: Long
        val vm: Long
        val vd: Long
        var s: String
        val nm: String
        if (f == 0L) return ""
        nm = monthnamesp(f)
        vy = f / 100000000
        f = f % 100000000
        vm = (f.toInt() / 1000000).toLong()
        f = f % 1000000
        vd = (f.toInt() / 10000).toLong()
        f = f % 10000
        s = ""
        s = if (vd > 9) "$s$vd/" else s + "0" + vd.toString() + "/"
        s = "$s$nm/20"
        s = if (vy > 9) s + vy.toString() else s + "0" + vy.toString()
        return s
    }

    fun dateDiff(f1: Long, f2: Long): Int {
        var f1 = f1
        var f2 = f2
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var date1: Date? = null
        var date2: Date? = null
        val fd: Long
        if (f1 <= 0) return -1
        if (f2 <= 0) return -1
        if (f1 < f2) {
            fd = f1
            f1 = f2
            f2 = fd
        }
        date1 = try {
            sdf.parse(sfecha(f1))
        } catch (e: Exception) {
            return -1
        }
        date2 = try {
            sdf.parse(sfecha(f2))
        } catch (e: Exception) {
            return -1
        }
        val diffInMillisec = date1!!.getTime() - date2!!.getTime()
        return TimeUnit.MILLISECONDS.toDays(diffInMillisec).toInt()
    }

    fun univfecha(f: Long): String {
        var f = f
        val vy: Long
        val vm: Long
        val vd: Long
        val m: Long
        val h: Long
        var s: String

        //yyyyMMdd hh:mm:ss
        vy = (f.toInt() / 100000000).toLong()
        f = f % 100000000
        vm = (f.toInt() / 1000000).toLong()
        f = f % 1000000
        vd = (f.toInt() / 10000).toLong()
        f = f % 10000
        h = (f.toInt() / 100).toLong()
        m = f % 100
        s = "20"
        s = if (vy > 9) s + vy else s + "0" + vy
        s = if (vm > 9) s + vm else s + "0" + vm
        s = if (vd > 9) s + vd else s + "0" + vd
        s = "$s "
        s = if (h > 9) s + h else s + "0" + h
        s = "$s:"
        s = if (m > 9) s + m else s + "0" + m
        s = "$s:00"
        return s
    }

    fun univfechaext(f: Int): String {
        var f = f
        val vy: Int
        val vm: Int
        val vd: Int
        var s: String

        //yyyyMMdd hh:mm:ss
        vy = f / 10000
        f = f % 10000
        vm = f / 100
        f = f % 100
        vd = f
        s = "" + vy
        s = if (vm > 9) s + vm else s + "0" + vm
        s = if (vd > 9) s + vd else s + "0" + vd
        s =
            "$vy $vm:$vd:00" //#HS_20181128_1102 Agregue " "+vm+":"+vd+":00" para que devolviera la hora.
        return s
    }

    fun fechames(f: Long): Long {
        var f = f
        f = (f.toInt() / 1000000).toLong()
        f = f * 1000000
        return f
    }

    fun ffecha00(f: Long): Long {
        var f = f
        f = f / 10000
        f = f * 10000
        return f
    }

    fun ffecha24(f: Long): Long {
        var f = f
        f = f / 10000
        f = f * 10000 + 2359
        return f
    }

    fun cfecha(year: Int, month: Int, day: Int): Long {
        var c: Long
        c = (year % 100).toLong()
        c = c * 10000 + month * 100 + day
        return c * 10000
    }

    fun cfechaint(year: Int, month: Int, day: Int): Int {
        var c: Long
        val cc: Int
        c = (year % 100).toLong()
        c = c * 10000 + month * 100 + day
        cc = c.toInt()
        return cc
    }

    fun parsedate(date: Long, hour: Int, min: Int): Long {
        val f: Long
        f = date + 100 * hour + min
        return f
    }

    fun getyear(f: Long): Int {
        var f = f
        var vy: Long
        vy = f / 100000000
        f = f % 100000000
        vy = vy + 2000
        return vy.toInt()
    }

    fun getmonth(f: Long): Int {
        var f = f
        val vy: Long
        val vm: Long
        vy = f / 100000000
        f = f % 100000000
        vm = f / 1000000
        f = f % 1000000
        return vm.toInt()
    }

    fun getweek(f: Long): Int {
        var cyear: Long
        var cmonth: Long
        var cday: Long

        val c = Calendar.getInstance()
        c[getyear(f), getmonth(f) - 1] = getday(f)

        return c.get(Calendar.WEEK_OF_YEAR)
    }

    fun getday(f: Long): Int {
        var f = f
        val vy: Long
        val vm: Long
        val vd: Long
        vy = f / 100000000
        f = f % 100000000
        vm = f / 1000000
        f = f % 1000000
        vd = f / 10000
        f = f % 10000
        return vd.toInt()
    }

    fun gethour(f: Long): Int {
        var f = f
        val vy: Int
        val vm: Int
        val vd: Int
        val vh: Int
        vy = f.toInt() / 100000000
        f = f % 100000000
        vm = f.toInt() / 1000000
        f = f % 1000000
        vd = f.toInt() / 10000
        f = f % 10000
        vh = f.toInt() / 100
        return vh
    }

    fun getmin(f: Long): Int {
        var f = f
        val vy: Int
        val vm: Int
        val vd: Int
        val vh: Int
        val vmi: Int
        vy = f.toInt() / 100000000
        f = f % 100000000
        vm = f.toInt() / 1000000
        f = f % 1000000
        vd = f.toInt() / 10000
        f = f % 10000
        vh = f.toInt() / 100
        vmi = f.toInt() % 100
        return vmi
    }

    fun getmindif(f1: Long, f2: Long): Int {
        var ff: Int
        val t1: Int
        val t2: Int
        var vh: Int
        var vmi: Int
        ff = f1.toInt() % 10000
        vh = ff / 100
        vmi = ff % 100
        t1 = 60 * vh + vmi
        ff = f2.toInt() % 10000
        vh = ff / 100
        vmi = ff % 100
        t2 = 60 * vh + vmi
        ff = t1 - t2
        if (ff < 0) ff = -ff
        return ff
    }

    fun LastDay(year: Int, month: Int): Int {
        val m: Int
        var y: Int
        var ld: Int
        m = month % 2
        ld = if (m == 1) {
            31
        } else {
            30
        }
        if (month == 2) {
            ld = 28
            if (year % 4 == 0) {
                ld = 29
            }
        }
        return ld
    }

    fun addDays(f: Long, days: Int): Long {
        var f = f
        val cyear: Long
        val cmonth: Long
        val cday: Long
        val c = Calendar.getInstance()
        c[getyear(f), getmonth(f) - 1] = getday(f)
        c.add(Calendar.DATE, days)
        cyear = c[Calendar.YEAR].toLong()
        cmonth = (c[Calendar.MONTH] + 1).toLong()
        cday = c[Calendar.DAY_OF_MONTH].toLong()
        f = cfecha(cyear.toInt(), cmonth.toInt(), cday.toInt())
        return f
    }

    fun dayweek(f: Long): String {
        val y: Int
        val m: Int
        val d: Int
        y = getyear(f)
        m = getmonth(f) - 1
        d = getday(f)
        val c: Calendar = GregorianCalendar(y, m - 1, d, 1, 0, 0)
        var dn = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        dn = dn.substring(0, 1).uppercase(Locale.getDefault()) + dn.substring(1)
            .lowercase(Locale.getDefault())
        return dn
    }

    fun dayofweek(f: Long): Int {
        var y: Int
        var m: Int
        var d: Int
        var dw: Int
        val c = Calendar.getInstance()
        c[getyear(f), getmonth(f) - 1] = getday(f)
        dw = c[Calendar.DAY_OF_WEEK]
        dw = if (dw == 1) 7 else dw - 1
        return dw
    }

    fun monthname(f: Long): String {
        val y: Int
        val m: Int
        val d: Int
        y = getyear(f)
        m = getmonth(f) - 1
        d = getday(f)
        val c: Calendar = GregorianCalendar(y, m, d, 1, 0, 0)
        var dn = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        dn = dn.substring(0, 1).uppercase(Locale.getDefault()) + dn.substring(1)
            .lowercase(Locale.getDefault())
        return dn
    }

    fun monthnamesp(f: Long): String {
        val y: Int
        val m: Int
        val d: Int
        val spanish = Locale("es", "ES")
        y = getyear(f)
        m = getmonth(f) - 1
        d = getday(f)
        val c: Calendar = GregorianCalendar(y, m, d, 1, 0, 0)
        var dn = c.getDisplayName(Calendar.MONTH, Calendar.LONG, spanish)
        dn = dn.substring(0, 1).uppercase(Locale.getDefault()) + dn.substring(1, 3)
            .lowercase(Locale.getDefault())
        return dn
    }

    fun nombremes(m: Int): String {
        var nm:String=""

        when (m) {
            1 -> {nm="Enero"}
            2 -> {nm="Febrero"}
            3 -> {nm="Marzo"}
            4 -> {nm="Abril"}
            5 -> {nm="Mayo"}
            6 -> {nm="Junio"}
            7 -> {nm="Julio"}
            8 -> {nm="Agosto"}
            9 -> {nm="Septiembre"}
           10 -> {nm="Octubre"}
           11 -> {nm="Noviembre"}
           12 -> {nm="Diciembre"}
        }

        return nm
    }

    fun dayweekshort(f: Long): String {
        val y: Int
        val m: Int
        val d: Int
        if (f == 0L) return ""
        y = getyear(f)
        m = getmonth(f)
        d = getday(f)
        val c: Calendar = GregorianCalendar(y, m - 1, d, 1, 0, 0)
        var dn = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
        dn = dn.substring(0, 1).uppercase(Locale.getDefault()) + dn.substring(1)
            .lowercase(Locale.getDefault())
        return dn
    }

    fun fechaapi(ff: Long): String {
        var f=ff
        val vy: Long
        val vm: Long
        val vd: Long
        val m: Long
        val h: Long
        var s: String

        //yyyyMMdd hh:mm:ss
        vy = (f / 100000000)
        f = f % 100000000
        vm = (f / 1000000)
        f = f % 1000000
        vd = (f / 10000)
        f = f % 10000
        h = 0
        m = 0
        s = "20"
        if (vy > 9) s+=""+vy else s+="0" + vy
        if (vm > 9) s+="-"+vm else s+="-0" + vm
        if (vd > 9) s+="-"+vd else s+="-0" + vd
        s = s+"T"
        if (h > 9) s+=""+h else s+="0"+h
        s = s+":"
        if (m > 9) s+=""+m else s+="0"+m
        s = s+":00.000Z"

        return s.toString()
    }

    fun fechaapif(ff: Long): String {
        var f=ff
        val vy: Long
        val vm: Long
        val vd: Long
        val m: Long
        val h: Long
        var s: String

        //yyyyMMdd hh:mm:ss
        vy = (f / 100000000)
        f = f % 100000000
        vm = (f / 1000000)
        f = f % 1000000
        vd = (f / 10000)
        f = f % 10000
        h = 23
        m = 59
        s = "20"
        if (vy > 9) s+=""+vy else s+="0" + vy
        if (vm > 9) s+="-"+vm else s+="-0" + vm
        if (vd > 9) s+="-"+vd else s+="-0" + vd
        s = s+"T"
        if (h > 9) s+=""+h else s+="0"+h
        s = s+":"
        if (m > 9) s+=""+m else s+="0"+m
        s = s+":00.000Z"

        return s.toString()
    }

    fun getBeginMonth(cyear: Int,cmonth: Int): Long {
        var dd: Long
        dd = cfecha(cyear, cmonth, 1)
        return dd
    }

    fun getEndMonth(cyear: Int,cmonth: Int): Long {
        var dd: Long;var dn: Long;
        dd = cfecha(cyear, cmonth, 1)

        dn=addDays(dd,32)
        var cy=getyear(dn)
        var cm=getmonth(dn)

        dd = cfecha(cy, cm, 1)
        return addDays(dd,-1)
    }

    val actDate: Long
        get() {
            val cyear: Long
            val cmonth: Long
            val cday: Long
            val f: Long
            val c = Calendar.getInstance()
            cyear = c[Calendar.YEAR].toLong()
            cmonth = (c[Calendar.MONTH] + 1).toLong()
            cday = c[Calendar.DAY_OF_MONTH].toLong()
            f = cfecha(cyear.toInt(), cmonth.toInt(), cday.toInt())
            return f
        }

    val actDateTime: Long
        get() {
            val cyear: Int
            val cmonth: Int
            val cday: Int
            val ch: Int
            val cm: Int
            var f: Long
            val c = Calendar.getInstance()
            cyear = c[Calendar.YEAR]
            cmonth = c[Calendar.MONTH] + 1
            cday = c[Calendar.DAY_OF_MONTH]
            ch = c[Calendar.HOUR_OF_DAY]
            cm = c[Calendar.MINUTE]
            f = cfecha(cyear, cmonth, cday)
            f = f + ch * 100 + cm
            return f
        }

    val actDateInt: Int
        get() {
            val cyear: Long
            val cmonth: Long
            val cday: Long
            val ff: Int
            val c = Calendar.getInstance()
            cyear = c[Calendar.YEAR].toLong()
            cmonth = (c[Calendar.MONTH] + 1).toLong()
            cday = c[Calendar.DAY_OF_MONTH].toLong()
            ff = cfechaint(cyear.toInt(), cmonth.toInt(), cday.toInt())
            return ff
        }

    val actWeek: Int
        get() {
            val cweek: Int
            val c = Calendar.getInstance()
            cweek = c[Calendar.WEEK_OF_YEAR]
            return cweek
        }

    fun getWeek(f: Long): Int {
        val cweek: Int
        val c = Calendar.getInstance()
        c[getyear(f), getmonth(f) - 1] = getday(f)
        cweek = c[Calendar.WEEK_OF_YEAR]
        return cweek
    }

    val zuluTime: Long
        get() {
            val cyear: Int
            val cmonth: Int
            val cday: Int
            val ch: Int
            val cm: Int
            var f: Long
            val c = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cyear = c[Calendar.YEAR]
            cmonth = c[Calendar.MONTH] + 1
            cday = c[Calendar.DAY_OF_MONTH]
            ch = c[Calendar.HOUR_OF_DAY]
            cm = c[Calendar.MINUTE]
            f = cfecha(cyear, cmonth, cday)
            f = f + ch * 100 + cm
            return f
        }

    val actDateStr: String
        get() {
            val cyear: Int
            val cmonth: Int
            val cday: Int
            val f: Long
            val c = Calendar.getInstance()
            cyear = c[Calendar.YEAR]
            cmonth = c[Calendar.MONTH] + 1
            cday = c[Calendar.DAY_OF_MONTH]
            f = cfecha(cyear, cmonth, cday)
            return sfecha(f)
        }

    val corelBase: Long
        get() {
            var cyear: Int
            val cmonth: Int
            val cday: Int
            val ch: Int
            val cm: Int
            val cs: Int
            val vd: Int
            val vh: Int
            var f: Long
            val c = Calendar.getInstance()
            cyear = c[Calendar.YEAR]
            cyear = cyear % 10
            cmonth = c[Calendar.MONTH] + 1
            cday = c[Calendar.DAY_OF_MONTH]
            ch = c[Calendar.HOUR_OF_DAY]
            cm = c[Calendar.MINUTE]
            cs = c[Calendar.SECOND]
            vd = cyear * 384 + cmonth * 32 + cday
            vh = ch * 3600 + cm * 60 + cs
            f = (vd * 100000 + vh).toLong()
            f = f * 100
            return f
        }

    val corelTimeStr: String
        get() {
            var cyear: Int
            val cmonth: Int
            val cday: Int
            val ch: Int
            val cm: Int
            val cs: Int
            val vd: Int
            val vh: Int
            val f: Long
            val c = Calendar.getInstance()
            cyear = c[Calendar.YEAR]
            cyear = cyear % 10
            cmonth = c[Calendar.MONTH] + 1
            cday = c[Calendar.DAY_OF_MONTH]
            ch = c[Calendar.HOUR_OF_DAY]
            cm = c[Calendar.MINUTE]
            cs = c[Calendar.SECOND]
            vd = cyear * 384 + cmonth * 32 + cday
            vh = ch * 3600 + cm * 60 + cs
            f = (vd * 100000 + vh).toLong()
            return "" + f
        }

    //region Fecha larga

    fun fechalarga(year: Int, month: Int, day: Int): Long {
        var c: Long
        c = (year % 10000).toLong()
        c = c * 10000 + month * 100 + day
        return c
    }

    fun sfechaLarga(f: Long): String {
        var f = f
        val vy: Long
        val vm: Long
        val vd: Long
        val sy: String
        val sm: String
        val sd: String
        if (f == 0L) return "--/--/--"
        vy = f / 10000
        f = f % 10000
        vm = f / 100
        vd = f % 100
        sy = "" + vy
        sm = if (vm > 9) "" + vm else "0$vm"
        sd = if (vd > 9) "" + vd else "0$vd"
        return "$sd/$sm/$sy"
    }

    fun sfechaLargalocal(f: Long): String {
        var f = f
        var vy: Int
        val vm: Int
        val vd: Int
        var sy: String
        var sm: String
        var sd: String
        if (f == 0L) return ""
        f = f / 10000
        vy = (f / 10000).toInt()
        vy = 2000 + vy
        f = f % 10000
        vm = (f / 100).toInt()
        vd = (f % 100).toInt()
        f = cfecha(vy, vm, vd)
        var s = sfecha(f)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(s)
            s = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
        } catch (e: Exception) {
            s = ""
        }
        return s
    }

    fun fechalargaYear(f: Long): Int {
        val vy = f / 10000
        return vy.toInt()
    }

    fun fechalargaMonth(f: Long): Int {
        var f = f
        f = f % 10000
        val vm = f / 100
        return vm.toInt()
    }

    fun fechalargaDia(f: Long): Int {
        var f = f
        val vm: Long
        val vd: Long
        f = f % 10000
        vm = f / 100
        vd = f % 100
        return vd.toInt()
    }

    fun fechalargaDate(): Long {
        val cyear: Int
        val cmonth: Int
        val cday: Int
        val f: Long
        val c = Calendar.getInstance()
        cyear = c[Calendar.YEAR]
        cmonth = c[Calendar.MONTH] + 1
        cday = c[Calendar.DAY_OF_MONTH]
        f = fechalarga(cyear, cmonth, cday)
        return f
    } //endregion
}