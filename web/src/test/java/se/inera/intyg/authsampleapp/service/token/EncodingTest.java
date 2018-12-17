package se.inera.intyg.authsampleapp.service.token;


import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class EncodingTest {

    @Test
    public void encodeAssertionToUrlEncoding() throws IOException {
        String base64assertion = IOUtils.toString(new ClassPathResource("working/Assertion_encoded.txt").getInputStream());
        String base64assertionUrlEncoded = IOUtils.toString(new ClassPathResource("working/assertion-after-urlencode.txt").getInputStream());

        String urlEncoded = URLEncoder.encode(base64assertion, "UTF-8");

        assertEquals(urlEncoded, base64assertionUrlEncoded);
    }

    @Test
    public void decodeToken() {
        String[] parts = "eyJraWQiOiI3MjUwNDg4ODc1MzI1MjMwOTQzNzQwMDM4MTQxMTkxNjg0IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJjb21taXNzaW9uUHVycG9zZSI6WyJBZG1pbmlzdHJhdGlvbiJdLCJzdWIiOiJlNjFmNjQ0MS1lODZkLTRkM2YtYTc3OC00MmE0YzM3ZmE3NDAiLCJoZWFsdGhDYXJlVW5pdE5hbWUiOlsiVsOlcmRjZW50cmFsIFbDpHN0Il0sIm1haWwiOlsiU0lUSFN0ZXN0QGluZXJhLnNlIl0sInBlcnNvbmFsSWRlbnRpdHlOdW1iZXIiOlsiMTk5MDAxMTQyMzgwIl0sImlzcyI6Imh0dHBzOlwvXC9pZHAuaW5lcmFkZXYub3JnOjQ0M1wvb2lkYyIsImhlYWx0aGNhcmVQcm92aWRlcklkIjpbIjQ0Nzc2Ni0zMzI0Il0sImF1dGhuTWV0aG9kIjpbInVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOlRMU0NsaWVudCJdLCJwYVRpdGxlQ29kZSI6WyIyMDEwMTEiXSwiY29tbWlzc2lvbkhzYUlkIjpbIlROVDQ0Nzc2NjMzMjItMTAwUjAyIl0sImFjciI6Imh0dHA6XC9cL2lkLnNhbWJpLnNlXC9sb2FcL2xvYTMiLCJzeXN0ZW1Sb2xlIjpbIkJJRjtTcMOkcnJhZG1pbmlzdHJhdMO2ciIsIlBVO1PDtmtuaW5nIiwiUFU7VGVzdHBlcnNvbmVyIiwiQklGO0xvZ2dhZG1pbmlzdHJhdMO2ciIsIkJJRjtTTVMiLCJCSUY7T3RwYWRtaW5pc3RyYXTDtnIiLCJCSUY7QWRtaW5pc3RyYXTDtnIiXSwib3JnYW5pemF0aW9uSWRlbnRpZmllciI6WyI0NDc3NjYtMzMyNCJdLCJzdXJuYW1lIjpbIkJvcmciXSwiYXpwIjoibHVwYW5kZXIiLCJhdXRoX3RpbWUiOiIxNTQ1MDQxNDgxNDU5IiwiaGVhbHRoQ2FyZVByb3ZpZGVyTmFtZSI6WyJMYW5kc3RpbmcgMSJdLCJoZWFsdGhDYXJlUHJvdmlkZXJIc2FJZCI6WyJUTlQ0NDc3NjYzMzIyLTAwMDIiXSwiZXhwIjoxNTQ1MDQ4NDM1LCJqdGkiOiJiOWY4NzZmMS0yMGQwLTQ1YWMtYTg2ZS05NmExZmI4ZmUxMWQiLCJwZXJzb25hbFByZXNjcmlwdGlvbkNvZGUiOlsiMTExMTEzNyJdLCJvcmdhbml6YXRpb25OYW1lIjpbIkxhbmRzdGluZyAxIl0sImhlYWx0aENhcmVVbml0SHNhSWQiOlsiVE5UNDQ3NzY2MzMyMi0xMDBSIl0sImdpdmVuTmFtZSI6WyJBbG1hIl0sIng1MDlTdWJqZWN0TmFtZSI6WyJTRVJJQUxOVU1CRVI9VFNUNTU2NTU5NDIzMC0xMFIzMDcyLCBFTUFJTEFERFJFU1M9U0lUSFN0ZXN0QGluZXJhLnNlLCBUPUzDpGthcmUsIEdJVkVOTkFNRT1BbG1hLCBTVVJOQU1FPUJvcmcsIENOPUFsbWEgQm9yZywgTz1UZXN0a29ydCwgTD1OYXRpb25lbGwgdGVzdCwgQz1TRSJdLCJsZXZlbE9mQXNzdXJhbmNlIjpbImh0dHA6XC9cL2lkLnNhbWJpLnNlXC9sb2FcL2xvYTMiXSwieDUwOUlzc3Vlck5hbWUiOlsiQ049U0lUSFMgVHlwZSAxIENBIHYxIFBQLE89SW5lcmEgQUIsQz1TRSJdLCJoZWFsdGhjYXJlUHJvZmVzc2lvbmFsTGljZW5zZSI6WyJMSyJdLCJjb21taXNzaW9uTmFtZSI6WyJBZG1pbmlzdHJhdGlvbiBMYW5kc3RpbmcgMSBWQyBWw6RzdCJdLCJhdWQiOlsiaHR0cHM6XC9cL3NwLmRldi5pbmVyYS50ZXN0Ojg4ODEiLCJodHRwczpcL1wvaWRwLmluZXJhZGV2Lm9yZzo0NDNcL29pZGMiLCJsdXBhbmRlciJdLCJlbXBsb3llZUhzYUlkIjpbIlRTVDU1NjU1OTQyMzAtMTBSMzA3MiJdLCJtb2JpbGVUZWxlcGhvbmVOdW1iZXIiOlsiMDcwMjQzMjU4NyJdfQ.Gh2qkQrbct64PsW6VjRXZ0Y8rUl3xdSOMxfGjmYTdIGWVsL0_iIiYyXJtuhtW-U883JBOcAXOezMdPXf2BIxKGOy_2dafSWP-CjhgZ27o0KwEWwxb3CB1YZmnpqd-wG4vj65w2nnum7yIIYEcKQPle7fUz-uUDrEXakvMsJ9XbceXQVOS7P1YdItLDRh02FKAzQsL4Ivw_SFEY5zXMZUHEEqIEpPMgNHBazPwXaumB5RI-hIppYX59mlLSsk3_lV4qSbzY84RFwrlvJupYe0LDbL3B4CLI7SH4NvDlP9tTNVBWAPZUvl7ci9_xwLuolay8riXSkCCM6TJ9DBTAugvogF81E000nQds4xgmN9VsfZZk_MP2lV5YPx6CcUnDtdGLfg2igYOf7y1CWwpVospad_JD-0XyFxth-TgQYuLy6ycUaFlm1Mu0woqUbt1WRYfa5JLGdkJ2qSYouXjMm9FvfJST7Xsc3Ooxy-8EB7EMA1idGf0wyogIrxJ7DctW6fT_fEXsW3OF3Bz_-xM5O6pcAgRA6qYXRg4C7HYogjciwyKnw5kdj-C3a4sPDR2rClHwpAFGMS7-xnxl7O0USeo4ZnBJpweDo3jsjXLHnZSeeRxQC8h8U2jr5Lze-W0aH-0E18LgVxjrStLXVs4tl_fDCdGIJBf5wh7WqcrgnDGQQ"
                .split("\\.");

//        for (String part : parts) {
//            byte[] decode = Base64.getDecoder().decode(part);
//            System.out.println(new String(decode, Charset.forName("UTF-8")));
//        }
    }

}
