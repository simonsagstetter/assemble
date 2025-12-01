import { useGetAllGreetingsQuery } from "@/api/graphql/queries/graphql";

export default async function Home() {
    const data = await useGetAllGreetingsQuery.fetcher()();

    return (
        <>
            { data.greetings.map( ( greeting, index ) => ( <p key={ index }>{ greeting.message }</p> ) ) }
        </>
    );
}
