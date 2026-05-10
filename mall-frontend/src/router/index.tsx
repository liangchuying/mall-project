import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Layout from '../layouts/Layout';
import Home from '../pages/Home';
import Demo from '../pages/Demo';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        index: true,
        element: <Home />,
      },
    ],
  },
  {
    path: '/demo',
    element: <Demo />,
  },
]);

export default function AppRouter() {
  return <RouterProvider router={router} />;
}
