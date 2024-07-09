package peliculasCac2024;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
/**
 * Servlet implementation class PeliculaServlet
 */


/* todo lo que enctra en /peliculas/* va a entrar en este servlet*/
@WebServlet("/peliculas/*")






public class PeliculaServlet extends HttpServlet {
	
	/*de peliculaService creo una instancia */
	private PeliculaService peliculaService;
	private ObjectMapper objectMapper;
	
	@Override
	public void init() throws ServletException
	{
		
		peliculaService =new PeliculaService();
		objectMapper = new ObjectMapper();
	}
	
	
	
	@Override
	/* este metodo solo sera accesible desde este mismo paquete. Este metodo lo obtengo de la clase httpservlet (el objeto es req)*/
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	/*para llegar a este metodo le tuvo que llegar una peticion http (get, post)*/, IOException
	
	/*tengo que capturar el pathinfo que es la url que me llego*/
	
	{
		String pathInfo=req.getPathInfo();

		
		try
		{
			
			if (pathInfo==null ||pathInfo.equals("/"))
			
			{
				List<Pelicula> peliculas=peliculaService.getAllPeliculas();
				String json = objectMapper.writeValueAsString(peliculas);
				resp.setContentType("application/json");
				resp.getWriter().write(json);
				
			}
			
			else 
			{
				String[] pathParts = pathInfo.split("/");
				
				/*convierto en entero lo que esta en la posición 1 para poder tomar el string, porque mi metodo es un string y yo necesito el entero*/
				int id= Integer.parseInt(pathParts[1]);
				Pelicula pelicula=peliculaService.getPeliculaById(id);
				if(pelicula!=null)
					
				{
					String json = objectMapper.writeValueAsString(pelicula);
					resp.setContentType("application/json");
					resp.getWriter().write(json);
				}
				else
				{
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					
				}
				
			}
		}
		catch (SQLException|ClassNotFoundException e )
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	/*para llegar a este metodo le tuvo que llegar una peticion http (get, post)*/, IOException
	{
		System.out.println("holaaa");
		

		/* que me lea todo lo que hay en el req de la clase pelicula*/
		Pelicula pelicula = objectMapper.readValue(req.getReader(), Pelicula.class);
		try {
			peliculaService.addPelicula(pelicula);
			resp.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (SQLException|ClassNotFoundException e )
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		}


	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	/*para llegar a este metodo le tuvo que llegar una peticion http (get, post)*/, IOException
	{
		
		try
		{
		/* que me lea todo lo que hay en el req de la clase pelicula*/
		Pelicula pelicula = objectMapper.readValue(req.getReader(), Pelicula.class);
		
		peliculaService.updatePelicula(pelicula);
		
		resp.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (SQLException|ClassNotFoundException e )
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		}
	
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	, IOException
	
	
	{
		String pathInfo=req.getPathInfo();
		try
		{
			/* && es "y" || es "o"*/
			if(pathInfo!=null&&pathInfo.split("/").length>1)
			{
				int id=Integer.parseInt(pathInfo.split("/")[1]);
				peliculaService.deletePelicula(id);
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
			else
			{
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		catch (SQLException|ClassNotFoundException e )
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		
	
	}
}
