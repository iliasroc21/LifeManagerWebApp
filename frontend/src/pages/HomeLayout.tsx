import {Outlet} from 'react-router-dom'
const HomeLayout = () => {
  return (
    <div>
      <section>
        <Outlet/>
      </section>
    </div>
  )
}

export default HomeLayout
