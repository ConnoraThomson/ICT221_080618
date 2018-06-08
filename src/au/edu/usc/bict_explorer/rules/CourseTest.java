package au.edu.usc.bict_explorer.rules;

import org.junit.jupiter.api.BeforeEach;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;


class CourseTest {

    private Map<String, Option> opts12;

    //public Course(String code, String name, String desc, String semesters, PreReqs pre, Option... downstream) {

    @BeforeEach
    void setUp() throws Exception {
        opts12 = new HashMap<>();
        opts12.put("OPT1", new Course("OPT1", "Option 1", "Desc 1", "123", new PreReqs(""), null));
        opts12.put("OPT2", new Course("OPT2", "Option 2", "Desc 2", "123", new PreReqs(""), null));
    }


    @Test
    void testEmpty() throws IOException, ParseException {
        File empty = new File("src/au/edu/usc/bict_explorer/rules/empty.options");
        Degree degree = new Degree(empty, empty, empty);
        assertEquals(0, degree.courses().size());
        assertEquals(0, degree.minors().size());
        assertEquals(0, degree.careers().size());
        assertEquals("", ((Course)degree.courses().get("OPT1")).getPreReqs().toString());
        assertEquals("", ((Course)degree.courses().get("OPT2")).getPreReqs().toString());
        assertEquals(0, ((Course)degree.courses().get("OPT1")).getDownstream().size());
        assertEquals(0, ((Course)degree.courses().get("OPT2")).getDownstream().size());
    }

    @Test

    void testReadOptionsMissing() throws IOException, ParseException {
        assertThrows(FileNotFoundException.class, () ->
                Degree.readOptions(new File("dummy.options"), null));
    }

    @Test
    void testReadOptionsBad() throws IOException, ParseException {
        assertThrows(ParseException.class, () ->
                Degree.readOptions(new File("src/au/edu/usc/bict_explorer/rules/bad.options"), null));
    }

    @Test
    void testReadOptionsBadSub() throws IOException, ParseException {
        ParseException ex = assertThrows(ParseException.class, () ->
                Degree.readOptions(new File("src/au/edu/usc/bict_explorer/rules/bad_sub.options"), opts12));
        assertEquals("Unknown downstream option: OPT3", ex.getMessage());
    }

    @Test
    void testReadOptions() throws IOException, ParseException {
        Map<String, Option> opts = Degree.readOptions(
                new File("src/au/edu/usc/bict_explorer/rules/good1.options"),
                opts12);
        assertEquals(2, opts.size());
        Course abc = (Course) opts.get("ABC");
        assertEquals("ABC", opts.get("ABC").getCode());
        assertEquals("A Better Class", abc.getName());
        assertEquals("ABC description", abc.getDescription());
        assertEquals("13", abc.getSemesters());
        Set<Option> subABC = abc.getDownstream();
        assertEquals(2, subABC.size());
        Iterator<Option> iter = subABC.iterator();
        assertEquals("OPT2", iter.next().getCode());
        assertEquals("OPT1", iter.next().getCode());
        Set<Option> subDEF = opts.get("DEF").getDownstream();
        assertEquals(0, subDEF.size());
    }

    @Test
    void testReadCourses() throws IOException, ParseException {
        Map<String, Option> opts = Degree.readOptions(
                new File("src/au/edu/usc/bict_explorer/rules/courses_test.options"),
                opts12);
        assertEquals(11, opts.size());
        Course c = (Course) opts.get("ICT310");
        assertEquals("1", c.getSemesters());
        assertTrue(c.getPreReqs().isSatisfied(opts));
        c= (Course) opts.get("ICT221");
        assertEquals("123", c.getSemesters());
        assertFalse(c.getPreReqs().isSatisfied(opts));
    }
}
