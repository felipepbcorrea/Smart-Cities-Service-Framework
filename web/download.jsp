<%@page import="java.io.InputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <%
        // File arquivo = (File) request.getAttribute("file2");
        //File arquivo = (File) session.getAttribute("file"+request.getAttribute("arquivo"));
        //File arquivo = new File(getServletContext().getRealPath("/"), request.getParameter("file"));
        File arquivo = (File) session.getAttribute("file"+ request.getParameter("numero"));
        if (!arquivo.exists()) {
    %>
    <script type="text/javascript"  language="javascript">
        alert('Arquivo desejado não encontrado. Verifique a geração do arquivo.');
    </script>
    <%                    } else {
        response.setContentType("application/octet-stream");
        // comento a linha abaixo para não forçar o download. vai abrir na própria janela.
        response.setHeader("Content-Disposition", "attachment; filename=" + arquivo.getName());

        InputStream in = new FileInputStream(arquivo);
        PrintWriter output = response.getWriter();

        int bit = 256;

        try {
            while ((bit) > 0) {
                bit = in.read();
                if (bit != -1) //esse teste é feito pq ao chegar ao fim do arquivo, in.read() retorna -1.
                {
                    output.write(bit);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        output.flush();
                    output.close();
                    in.close();
                }

        
    %>
    

</html>
